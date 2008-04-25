/*
 *     StorageSet.java
 *     Copyright (C) 2008  Asger Blekinge-Rasmussen
 * 
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 * 
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package dk.statsbiblioteket.jpar2.sets;

import dk.statsbiblioteket.jpar2.checksum.CRC32;
import dk.statsbiblioteket.jpar2.checksum.MD5;
import dk.statsbiblioteket.jpar2.files.DataFile;
import dk.statsbiblioteket.jpar2.files.RecoveryData;
import dk.statsbiblioteket.jpar2.par2packets.CreatorPacket;
import dk.statsbiblioteket.jpar2.par2packets.FileDescriptionPacket;
import dk.statsbiblioteket.jpar2.par2packets.InvalidPacketException;
import dk.statsbiblioteket.jpar2.par2packets.MainPacket;
import dk.statsbiblioteket.jpar2.par2packets.RecoverySlicePacket;
import dk.statsbiblioteket.jpar2.par2packets.SliceChecksumPacket;
import dk.statsbiblioteket.jpar2.par2packets.UnparsedPacket;
import dk.statsbiblioteket.jpar2.par2packets.headers.PacketType;
import dk.statsbiblioteket.jpar2.par2packets.headers.RecoverySetID;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Head of and interface to the par2 packet storage structure. Use this class
 * to read and store a Par2Set as classical Par2packets.
 * @author Asger Blekinge-Rasmussen
 */
public class StorageSet {

    private Par2Set set;
    private static final String default_client = "JavaPar2 0.1.0";
    private boolean keep_client = false;

    public StorageSet(Par2Set set) {
        this.set = set;
    }

    public void keepClient(boolean keep) {
        this.keep_client = keep;
    }

    public List<ByteBuffer> writeData() {
        List<DataFile> datafiles = set.getDataFiles();
        List<RecoveryData> parity = set.getParityBlocks();
        int sliceSize = set.getSliceSize();

        //temporary recoverySetID
        RecoverySetID id = new MainPacket(sliceSize).calculateRecoverySetID();

        List<FileDescriptionPacket> fdp = new ArrayList<FileDescriptionPacket>();
        List<SliceChecksumPacket> scp = new ArrayList<SliceChecksumPacket>();

        List<RecoverySlicePacket> rsp = new ArrayList<RecoverySlicePacket>();

        for (DataFile df : datafiles) {
            FileDescriptionPacket f = new FileDescriptionPacket(id, df);
            SliceChecksumPacket s = new SliceChecksumPacket(f, df);
            fdp.add(f);
            scp.add(s);
        }
        for (RecoveryData rec : parity) {
            rsp.add(new RecoverySlicePacket(id, rec));
        }

        MainPacket m = new MainPacket(sliceSize);
        CreatorPacket c;
        if (this.keep_client) {
            c = new CreatorPacket(id, set.getClient());
        } else {
            c = new CreatorPacket(id, default_client);
        }


        //TODO nonrecoverySet
        for (FileDescriptionPacket f : fdp) {
            m.addFileId(f.getFileID(), true);
        }

        //now calculate the real recoverySetID
        id = m.calculateRecoverySetID();

        List<ByteBuffer> packetlist = new ArrayList<ByteBuffer>();

        //update all the packets and dump them
        m.setRecoverySetId(id);
        packetlist.add(m.writePacket());

        c.setRecoverySetId(id);
        packetlist.add(c.writePacket());

        for (FileDescriptionPacket f : fdp) {
            f.setRecoverySetId(id);
            packetlist.add(f.writePacket());
        }
        for (SliceChecksumPacket s : scp) {
            s.setRecoverySetId(id);
            packetlist.add(s.writePacket());
        }
        for (RecoverySlicePacket r : rsp) {
            r.setRecoverySetId(id);
            packetlist.add(r.writePacket());
        }

        return packetlist;
    }

    public static Par2Set readFromFiles(List<File> parFiles, File parentDir)
            throws InvalidSetException, FileNotFoundException, IOException {

        List<UnparsedPacket> packetList =
                new ArrayList<UnparsedPacket>();
        for (File parfile : parFiles) {
            //TODO files larger than 2 GB
            ByteBuffer buffer = new FileInputStream(parfile).getChannel().
                    map(
                    FileChannel.MapMode.READ_ONLY, 0, parfile.length());
            try {
                // The buffer is now read into the packet list
                while (buffer.hasRemaining()) {
                    packetList.add(new UnparsedPacket(buffer));
                }

            } catch (InvalidPacketException e) {
                throw new InvalidSetException("There was a problem loading the " +
                        "packets", e);
            }
        }

        //now we have all the packets in a file
        //now we must ensure that they are all from the same set.

        Map<RecoverySetID, List<UnparsedPacket>> setsort =
                new HashMap<RecoverySetID, List<UnparsedPacket>>();

        for (UnparsedPacket p : packetList) {
            RecoverySetID id = p.getRecoverySetId();
            if (!setsort.containsKey(id)) {
                setsort.put(id, new ArrayList<UnparsedPacket>());
            }
            setsort.get(id).add(p);
        }

        //now the packets are sorted.
        if (setsort.keySet().size() != 1) {
            throw new InvalidSetException("There were multiple sets in the" +
                    " specified files");
        }

        RecoverySetID key = (RecoverySetID) setsort.keySet().toArray()[0];
        return parsePacketList(setsort.get(key), parentDir);

    }

    /**
     * Loads in a new PacketSet, from the given packets
     * @param packetList A list of UnparsetPacket, all with the same RecoverySet
     * ID
     * @throws javapar2.Exceptions.InvalidSetException if the packetList:
     * <ul>
     * <li> does not contain a main packet</li>
     * <li> contain more than one main packet </li>
     * <li> does not contain a creator packet</li>
     * <li> contain more than one creator packet</li>
     * <li> contain a packet that cannot be parsed</li>
     * </ul>
     */
    private static Par2Set parsePacketList(List<UnparsedPacket> packetList,
            File parentDir)
            throws InvalidSetException {
        //Sort the packets
        List<CreatorPacket> creatorList = new ArrayList<CreatorPacket>();

        List<MainPacket> mainList = new ArrayList<MainPacket>();

        List<FileDescriptionPacket> fileList =
                new ArrayList<FileDescriptionPacket>();

        List<SliceChecksumPacket> sliceList =
                new ArrayList<SliceChecksumPacket>();

        List<RecoverySlicePacket> recoveryList =
                new ArrayList<RecoverySlicePacket>();

        List<UnparsedPacket> otherList = new ArrayList<UnparsedPacket>();

        for (UnparsedPacket p : packetList) {
            PacketType type = p.getPacketType();
            try {
                switch (type) {
                    case Creator:
                        creatorList.add(new CreatorPacket(p));
                        break;
                    case Main:
                        mainList.add(new MainPacket(p));
                        break;
                    case FileDescription:
                        //alternatively, pass the parent dir downwards 
                        //in this file instead
                        fileList.add(new FileDescriptionPacket(p, parentDir));
                        break;
                    case SliceChecksum:
                        sliceList.add(new SliceChecksumPacket(p));
                        break;
                    case RecoverySlice:
                        recoveryList.add(new RecoverySlicePacket(p));
                        break;
                    default:
                        otherList.add(p);
                        break;
                }
            } catch (InvalidPacketException e) {
                throw new InvalidSetException("A packet in the set could not " +
                        "be parsed", e);
            }
        }

        if (mainList.size() != 1) {
            throw new InvalidSetException("The set contains more than one " +
                    "main packet");
        }

        if (creatorList.size() != 1) {
            throw new InvalidSetException("The set contains more than one " +
                    "creator packet");
        }

        return createPar2Set(mainList.get(0), creatorList.get(0),
                fileList, sliceList, recoveryList);
    }

    private static Par2Set createPar2Set(MainPacket m, CreatorPacket c,
            List<FileDescriptionPacket> fdp,
            List<SliceChecksumPacket> scp,
            List<RecoverySlicePacket> rsp) throws InvalidSetException {


        Par2Set par2set = new Par2Set(m.getSliceSize());
        int sliceSize = par2set.getSliceSize();
        
        par2set.setClient(c.getClient());

        for (FileDescriptionPacket f : fdp) {
            boolean slicefound = false;
            for (SliceChecksumPacket s : scp) {
                if (f.getFileID().equals(s.getFileID())) {
                    par2set.addDataFile(constructDataFile(f, s, sliceSize));
                    scp.remove(s);
                    slicefound = true;
                    break;
                }
            //so, take next slice packet
            }
            if (!slicefound) {
                throw new InvalidSetException("There were unmatched file " +
                        "description packets in the set");
            }
        //no slice packet matched, problem
        }
        if (scp.size() != 0) {
            throw new InvalidSetException("There were unmatched slice checksum" +
                    " packets in the set");
        }


        //fill the par2set with the parity data
        for (RecoverySlicePacket r : rsp) {
            par2set.addParityBlock(r.getRecoveryData());
        }

        return par2set;

    }

    /**
     * Utility method for constructing a DataFile object from packets
     * @param fdp
     * @param scp
     * @return a new DataFile
     */
    private static DataFile constructDataFile(
            FileDescriptionPacket fdp,
            SliceChecksumPacket scp, int sliceSize) {
        File file = fdp.getFile();
        long length = fdp.getLengthOfFile();
        MD5 hash = fdp.getMd5Hash();
        MD5 hash16k = fdp.getMd5Hash16k();
        List<MD5> hashes = new ArrayList<MD5>();
        hashes.addAll(scp.getHashes());
        List<CRC32> crc32s = new ArrayList<CRC32>();
        crc32s.addAll(scp.getCRC32s());
        return new DataFile(file, sliceSize, hash16k, hash, length, hashes, crc32s);

    }
}
