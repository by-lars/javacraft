package de.lars.javacraft.graphics.geometry;

import de.lars.javacraft.world.Chunk;
import de.lars.javacraft.world.Material;

import org.lwjgl.glfw.*;

import javax.management.MBeanAttributeInfo;
import java.util.Arrays;

import static java.lang.System.currentTimeMillis;

public class ChunkMesher {
    enum Method {
        BASIC,
        GREEDY
    }
    private static final Method m_MeshingMethod = Method.GREEDY;

    public static Mesh mesh(Chunk chunk) {
        return switch (m_MeshingMethod)  {
            case BASIC -> meshChunkBasic(chunk);
            case GREEDY -> meshChunkGreedy(chunk);
        };
    }
    private static Mesh meshChunkGreedy(Chunk chunk) {
        final int[] CHUNK_DIMENSIONS = {Chunk.DIM_X ,Chunk.DIM_Y, Chunk.DIM_Z};

        Mesh mesh = new Mesh(Chunk.DIM_X * Chunk.DIM_Y * Chunk.DIM_Z);

        boolean[] mask = new boolean[Chunk.DIM_X * Chunk.DIM_Y];

        int[] x = new int[3];
        int[] q = new int[3]; // (1, 0, 0); (0, 1, 0); (0, 0, 1);

        int[] du = new int[3];
        int[] dv = new int[3];

        int numFaces = 0;

        for (int d = 0; d < 3; d++) {
            Arrays.fill(mask, false);
            Arrays.fill(x, 0);
            Arrays.fill(q, 0);

            int w = 0;
            int h = 0;
            int k = 0;

            int u = (d + 1) % 3; // 1 2 0
            int v = (d + 2) % 3; // 2 0 1

            q[d] = 1;

            //Go through each slice of the chunk
            // (di, u, v); (v, di, u); (u, v, di);
            for (x[d] = -1; x[d] < CHUNK_DIMENSIONS[d];) {

                //Create the mask
                int n = 0;
                for (x[v] = 0; x[v] < CHUNK_DIMENSIONS[v]; x[v]++) {
                    for (x[u] = 0; x[u] < CHUNK_DIMENSIONS[u]; x[u]++) {
                        Material voxel = Material.AIR;
                        Material nextVoxel = (x[d] < CHUNK_DIMENSIONS[d] - 1) ? chunk.getVoxel(x[0] + q[0], x[1] + q[1], x[2] + q[2]) : Material.AIR;

                        if(x[d] != -1) {
                            voxel = (x[d] >= 0) ? chunk.getVoxel(x[0], x[1], x[2]) : Material.AIR;
                        }

                        boolean isVoxelSolid = (voxel == Material.AIR);
                        boolean isNextVoxelSolid = (nextVoxel == Material.AIR);

                        mask[n++] = (isVoxelSolid != isNextVoxelSolid);
                    }
                }

                x[d]++;
                n = 0;

                //Generate the mesh from the mask
                for (int j = 0; j < CHUNK_DIMENSIONS[v]; j++) {
                    for (int i = 0; i < CHUNK_DIMENSIONS[u];) {

                        if (mask[n]) {

                            //Compute the width of the quad/run
                            for (w = 1; i + w < CHUNK_DIMENSIONS[u] && mask[n + w] && mask[n] == mask[n + w]; w++) {}

                            boolean done = false;
                            for (h = 1; j + h < CHUNK_DIMENSIONS[v]; h++) {
                                for (k = 0; k < w; k++) {
                                    boolean f = mask[n + k + h * CHUNK_DIMENSIONS[u]];
                                    if (!f || f != mask[n]) {
                                        done = true;
                                        break;
                                    }
                                }

                                if (done)
                                    break;
                            }

                            x[u] = i;
                            x[v] = j;

                            Arrays.fill(du, 0); du[u] = w;
                            Arrays.fill(dv, 0); dv[v] = h;

                            mesh.vertices.put(new float[]{
                                    x[0] + du[0],			x[1] + du[1],			x[2] + du[2],			//w, h, // Top Right   corner
                                    x[0],					x[1],					x[2],					//0, h, // Top Left    corner
                                    x[0] + du[0] + dv[0],	x[1] + du[1] + dv[1],	x[2] + du[2] + dv[2],	//w, 0, // Lower right corner

                                    //x[0] + du[0] + dv[0],	x[1] + du[1] + dv[1],	x[2] + du[2] + dv[2],	//w, 0, // Lower right corner
                                    //x[0],					x[1],					x[2],					//0, h, // Top Left    corner
                                    x[0] + dv[0],			x[1] + dv[1],			x[2] + dv[2],			//0, 0, // Lower left  corner
                            });

                            mesh.indices.put(new int[]{
                                    0 + numFaces,
                                    1 + numFaces,
                                    2 + numFaces,
                                    2 + numFaces,
                                    1 + numFaces,
                                    3 + numFaces
                            });

                            mesh.numVertices += 4;
                            mesh.numIndices += 6;
                            numFaces += 4;

                            for (int l = 0; l < h; l++)
                                for (int f = 0; f < w; f++)
                                    mask[n + f + l * CHUNK_DIMENSIONS[u]] = false;

                            i += w;
                            n += w;

                        }
                        else {
                            i++;
                            n++;
                        }
                    }
                }
            }
        }

        return mesh;
    }

    private static Mesh meshChunkBasic(Chunk chunk) {
        Mesh mesh = new Mesh(Chunk.DIM_X * Chunk.DIM_Y * Chunk.DIM_Z * 4 * 6);

        for(int x = 0; x < Chunk.DIM_X; x++) {
            for(int y = 0; y < Chunk.DIM_Y; y++) {
                for(int z = 0; z < Chunk.DIM_Z; z++) {

                    if(chunk.getVoxel(x,y,z) == Material.AIR)
                        continue;

                    long milis = currentTimeMillis();
                    double o = milis;
                    o *= 0.002f;
                    float offset = (float)Math.sin(o + x+ +y +z)* 0.5f;
                    mesh.vertices.put(new float[]{
                            0 + x*1.4f,   0 + y*1.4f + offset, 1 + z*1.4f,  //0
                            1 + x*1.4f,   0 + y*1.4f + offset, 1 + z*1.4f,  //1
                            0 + x*1.4f,   1 + y*1.4f + offset, 1 + z*1.4f,  //2
                            1 + x*1.4f,   1 + y*1.4f + offset, 1 + z*1.4f,  //3
                            0 + x*1.4f,   0 + y*1.4f + offset, 0 + z*1.4f,  //4
                            1 + x*1.4f,   0 + y*1.4f + offset, 0 + z*1.4f,  //5
                            0 + x*1.4f,   1 + y*1.4f + offset, 0 + z*1.4f,  //6
                            1 + x*1.4f,   1 + y*1.4f + offset, 0 + z*1.4f  //7)
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
