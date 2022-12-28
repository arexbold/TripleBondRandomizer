package com.dabomstew.pkrandom.hac.unity;

import sun.reflect.generics.tree.TypeTree;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class AssetsFile {

    SerializedFile f;
    private static final int alignBytesMask = 0x4000;

    public AssetsFile(byte[] data) {
        f = new SerializedFile(ByteBuffer.wrap(data));
    }

    private class SerializedFile {

        ByteBuffer bb;
        SerializedFileHeader header;
        String unityVersion;
        int targetPlatform;
        boolean enableTypeTree, bigIDEnabled;
        SerializedType[] types;
        Map<Long,AssetObject> objects;

        private SerializedFile(ByteBuffer bb) {
            this.bb = bb;
            header = new SerializedFileHeader(bb);

            if (header.version >= 9) {
                if (bb.get() == 0) {
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                }
                bb.get(header.reserved);
                if (header.version >= 22) {
                    header.metadataSize = bb.getInt();
                    header.fileSize = bb.getLong();
                    header.dataOffset = bb.getLong();
                    bb.getLong();
                }
            } else {
                bb.position((int)header.fileSize - header.metadataSize);
                if (bb.get() != 0) {
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                }
            }

            if (header.version >= 7) {
                unityVersion = getString(bb);
            }

            if (header.version >= 8) {
                targetPlatform = bb.getInt();
            }

            if (header.version >= 13) {
                enableTypeTree = bb.get() != 0;
            }

            int typeCount = bb.getInt();
            types = new SerializedType[typeCount];
            for (int i = 0; i < typeCount; i++) {
                types[i] = new SerializedType(bb,header.version,enableTypeTree,false);
            }

            bigIDEnabled = false;
            if (header.version >= 7 && header.version < 14) {
                bigIDEnabled = bb.getInt() != 0;
            }

            objects = new HashMap<>();
            int objectCount = bb.getInt();
            for (int i = 0; i < objectCount; i++) {
                AssetObject obj = new AssetObject(bb,header.version,types,header.dataOffset);
                objects.put(obj.pathID,obj);
            }
        }
    }

    private class SerializedFileHeader {

        int metadataSize, version;
        long fileSize, dataOffset;
        byte[] reserved = new byte[3];
        private SerializedFileHeader(ByteBuffer bb)  {
            metadataSize = bb.getInt();
            fileSize = bb.getInt();
            version = bb.getInt();
            dataOffset = bb.getInt();
        }
    }

    private class SerializedType {

        int classId, scriptTypeIndex;
        boolean isStrippedType;
        byte[] scriptId = new byte[16];
        byte[] oldTypeHash = new byte[16];
        String className, namespace, assemblyName;
        int[] typeDependencies;
        byte[] stringData;
        List<TypeTreeNode> nodes;

        private SerializedType(ByteBuffer bb, int version, boolean enableTypeTree, boolean isRefType) {
            classId = bb.getInt();
            if (version >= 16) {
                isStrippedType = bb.get() != 0;
            }
            if (version >= 17) {
                scriptTypeIndex = bb.getShort();
            }
            if (version >= 13) {
                if ((isRefType && scriptTypeIndex >= 0) ||
                        (version < 16 && classId < 0) ||
                        (version >= 16 && classId == 114))  {
                    bb.get(scriptId);
                }
                bb.get(oldTypeHash);
            }
            if (enableTypeTree) {
                nodes = new ArrayList<>();
                if (version >= 12 || version == 10) {
                    stringData = readTypeTreeBlob(bb,version,nodes);
                } else {
                    System.err.println("help");
                    // give up
                }

                if (version >= 21) {
                    if (isRefType) {
                        className = getString(bb);
                        namespace = getString(bb);
                        assemblyName = getString(bb);
                    } else {
                        int arrLength = bb.getInt();
                        typeDependencies = new int[arrLength];
                        for (int i = 0; i < arrLength; i++) {
                            typeDependencies[i] = bb.getInt();
                        }
                    }
                }
            }
        }
    }

    private String getString(ByteBuffer bb) {
        int stringStart = bb.position();
        int stringLength = 0;
        while (bb.get() != 0) {
            stringLength++;
        }
        byte[] stringBytes = new byte[stringLength];
        bb.position(stringStart);
        bb.get(stringBytes);
        bb.get();   // Null terminator
        return new String(stringBytes);
    }

    private void alignByteBuffer(ByteBuffer bb, int alignment) {
        bb.position(bb.position() + ((alignment - (bb.position() % alignment)) % alignment));
    }

    private byte[] readTypeTreeBlob(ByteBuffer bb, int version, List<TypeTreeNode> nodes) {
        int nodeCount = bb.getInt();
        int stringBufferSize = bb.getInt();

        int nodeSize = 0x18;
        if (version >= 19) {
            nodeSize = 0x20;
        }

        int originalPos = bb.position();
        bb.position(originalPos + nodeSize * nodeCount);
        byte[] stringData = new byte[stringBufferSize];
        bb.get(stringData);
        ByteBuffer stringBB = ByteBuffer.wrap(stringData);
        bb.position(originalPos);

        for (int i = 0; i < nodeCount; i++) {
            nodes.add(new TypeTreeNode(bb, stringBB, version));
        }
        bb.position(bb.position() + stringBufferSize);
        return stringData;
    }

    private class TypeTreeNode {

        String type, name;
        int byteSize, index, version, metaFlag, level, typeStrOffset, nameStrOffset, typeFlags, variableCount;
        long refTypeHash;

        private TypeTreeNode(ByteBuffer dataBB, ByteBuffer stringBB, int headerVersion) {
            version = dataBB.getShort();
            level = dataBB.get() & 0xFF;
            typeFlags = dataBB.get() & 0xFF;
            typeStrOffset = dataBB.getInt();
            nameStrOffset = dataBB.getInt();
            byteSize = dataBB.getInt();
            index = dataBB.getInt();
            metaFlag = dataBB.getInt();
            if (headerVersion >= 19) {
                refTypeHash = dataBB.getLong();
            }
            if ((typeStrOffset & 0x80000000) != 0) {
                typeStrOffset &= 0x7FFFFFFF;
                type = CommonString.strings.get(typeStrOffset);
            } else {
                stringBB.position(typeStrOffset);
                type = getString(stringBB);
            }
            if ((nameStrOffset & 0x80000000) != 0) {
                nameStrOffset &= 0x7FFFFFFF;
                name = CommonString.strings.get(nameStrOffset);
            } else {
                stringBB.position(nameStrOffset);
                name = getString(stringBB);
            }
        }
    }

    private class AssetObject {

        long pathID, byteStart, byteHeaderOffset, byteBaseOffset, classID;
        SerializedType type;

        private AssetObject(ByteBuffer bb, int version, SerializedType[] types, long dataOffset) {
            alignByteBuffer(bb,4);
            pathID = bb.getLong();

            if (version >= 22) {
                byteStart = bb.getLong();
            } else {
                byteStart = bb.getInt();
            }
            byteStart += dataOffset;
            byteHeaderOffset = dataOffset;
            byteBaseOffset = bb.position();
            int byteSize = bb.getInt();
            int typeID = bb.getInt();
            type = types[typeID];
            classID = type.classId;
        }
    }

    public Map<String,Object> readTypeTree(long pathID) {
        AssetObject obj = f.objects.get(pathID);
        if (obj == null) {
            return null;
        }

        ByteBuffer bb = f.bb;
        bb.position((int)obj.byteStart);
        List<TypeTreeNode> nodes = obj.type.nodes;

        return (Map<String,Object>)readValue(nodes, bb, new WrappedInt(0));
    }

    private Object readValue(List<TypeTreeNode> nodes, ByteBuffer bb, WrappedInt i) {
        TypeTreeNode node = nodes.get(i.val);
        boolean align = (node.metaFlag & 0x4000) != 0;
        Object value;

        switch(node.type) {
            case "SInt8":
            case "UInt8":
            case "char":
                value = bb.get();
                break;
            case "short":
            case "SInt16":
            case "UInt16":
            case "unsigned short":
                value = bb.getShort();
                break;
            case "int":
            case "SInt32":
            case "UInt32":
            case "unsigned int":
            case "Type*":
                value = bb.getInt();
                break;
            case "long long":
            case "SInt64":
            case "UInt64":
            case "unsigned long long":
            case "FileSize":
                value = bb.getLong();
                break;
            case "float":
                value = bb.getFloat();
                break;
            case "double":
                value = bb.getDouble();
                break;
            case "bool":
                value = bb.get() != 0;
                break;
            case "string":
                i.val += 3;
                int length = bb.getInt();
                if (length > 0) {
                    byte[] strBytes = new byte[length];
                    bb.get(strBytes);
                    alignByteBuffer(bb,4);
                    value = new String(strBytes);
                } else {
                    value =  "";
                }
                break;
            case "map":
                if ((nodes.get(i.val + 1).metaFlag & alignBytesMask) != 0) {
                    align = true;
                }
                List<TypeTreeNode> subNodes = getNodes(nodes, i.val);
                i.val += (subNodes.size() - 1);
                List<TypeTreeNode> first = getNodes(subNodes, 4);
                List<TypeTreeNode> second = getNodes(subNodes, 4 + first.size());
                int size = bb.getInt();
                value = new HashMap<>();
                for (int j = 0; j < size; j++) {
                    Object key = readValue(first, bb, new WrappedInt(0));
                    ((Map<Object,Object>) value).put(key,readValue(second, bb, new WrappedInt(0)));
                }
                break;
            case "TypelessData":
                size = bb.getInt();
                value = new byte[size];
                bb.get((byte[])value);
                i.val += 2;
                break;
            default:
                if (i.val < nodes.size() - 1 && nodes.get(i.val + 1).type.equals("Array")) {
                    if ((nodes.get(i.val + 1).metaFlag & alignBytesMask) != 0) {
                        align = true;
                    }
                    List<TypeTreeNode> vector = getNodes(nodes, i.val);
                    i.val += (vector.size() - 1);
                    size = bb.getInt();
                    value = new Object[size];
                    for (int j = 0; j < size; j++) {
                        ((Object[])value)[j] = readValue(vector, bb, new WrappedInt(3));
                    }
                } else {
                    List<TypeTreeNode> classNodes = getNodes(nodes, i.val);
                    i.val += (classNodes.size() - 1);
                    value = new HashMap<>();
                    WrappedInt j = new WrappedInt(1);
                    while (j.val < classNodes.size()) {
                        TypeTreeNode classNode = classNodes.get(j.val);
                        ((Map<String, Object>) value).put(classNode.name,readValue(classNodes,bb,j));
                        j.val += 1;
                    }
                }
                break;
        }
        if (align) {
            alignByteBuffer(bb, 4);
        }
        return value;
    }

    private List<TypeTreeNode> getNodes(List<TypeTreeNode> nodes, int index) {
        int level = nodes.get(index).level;
        for (int i = index + 1; i < nodes.size(); i++) {
            if (nodes.get(i).level <= level) {
                return nodes.subList(index,i);
            }
        }
        return nodes.subList(index,nodes.size());
    }

    private class WrappedInt {
        int val;

        private WrappedInt(int val) {
            this.val = val;
        }
    }

    public Set<Long> getPathIDs() {
        return f.objects.keySet();
    }
}
