package de.lars.javacraft.graphics.geometry;

import de.lars.javacraft.world.Chunk;
import de.lars.javacraft.world.Material;

import org.lwjgl.glfw.*;

import static java.lang.System.currentTimeMillis;

public class ChunkMesher {
    enum Method {
        BASIC,
        GREEDY
    }
    private static final Method m_MeshingMethod = Method.BASIC;

    public static Mesh mesh(Chunk chunk) {
        return switch (m_MeshingMethod)  {
            case BASIC -> meshChunkBasic(chunk);
            case GREEDY -> throw new UnsupportedOperationException("Not Implemented");
        };
    }

    private static Mesh meshChunkBasic(Chunk chunk) {
        Mesh mesh = new Mesh(Chunk.DIM_X * Chunk.DIM_Y * Chunk.DIM_Z * 4 * 6);

        for(int x = 0; x < Chunk.DIM_X; x++) {
            for(int y = 0; y < Chunk.DIM_Y; y++) {
                for(int z = 0; z < Chunk.DIM_Z; z++) {

                    if(chunk.getVoxel(x,y,z) == Material.AIR)
                        continue;

                    long milis = currentTimeMillis();
                    float offset = (float)Math.sin(milis + x+ +y +z) * 5.0f;
                    mesh.vertices.put(new float[]{
                            0 + x*1.2f,   0 + y*1.2f + offset, 1 + z*1.2f,  //0
                            1 + x*1.2f,   0 + y*1.2f + offset, 1 + z*1.2f,  //1
                            0 + x*1.2f,   1 + y*1.2f + offset, 1 + z*1.2f,  //2
                            1 + x*1.2f,   1 + y*1.2f + offset, 1 + z*1.2f,  //3
                            0 + x*1.2f,   0 + y*1.2f + offset, 0 + z*1.2f,  //4
                            1 + x*1.2f,   0 + y*1.2f + offset, 0 + z*1.2f,  //5
                            0 + x*1.2f,   1 + y*1.2f + offset, 0 + z*1.2f,  //6
                            1 + x*1.2f,   1 + y*1.2f + offset, 0 + z*1.2f  //7)
                    });

                    mesh.indices.put(new int[]{
                            //Top
                            2 + mesh.numVertices, 6 + mesh.numVertices, 7 + mesh.numVertices,
                            2 + mesh.numVertices, 3 + mesh.numVertices, 7 + mesh.numVertices,

                            //Bottom
                            0 + mesh.numVertices, 4 + mesh.numVertices, 5 + mesh.numVertices,
                            0 + mesh.numVertices, 1 + mesh.numVertices, 5 + mesh.numVertices,

                            //Left
                            0 + mesh.numVertices, 2 + mesh.numVertices, 6 + mesh.numVertices,
                            0 + mesh.numVertices, 4 + mesh.numVertices, 6 + mesh.numVertices,

                            //Right
                            1 + mesh.numVertices, 3 + mesh.numVertices, 7 + mesh.numVertices,
                            1 + mesh.numVertices, 5 + mesh.numVertices, 7 + mesh.numVertices,

                            //Front
                            0 + mesh.numVertices, 2 + mesh.numVertices, 3 + mesh.numVertices,
                            0 + mesh.numVertices, 1 + mesh.numVertices, 3 + mesh.numVertices,

                            //Back
                            4 + mesh.numVertices, 6 + mesh.numVertices, 7 + mesh.numVertices,
                            4 + mesh.numVertices, 5 + mesh.numVertices, 7 + mesh.numVertices
                    });
                    mesh.numVertices += 8;
                    mesh.numIndices += 36;
                }
            }
        }

        return mesh;
    }
}
