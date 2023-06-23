package de.lars.javacraft.world;

public class Chunk {
    public static final int DIM_X = 32;
    public static final int DIM_Y = 32;
    public static final int DIM_Z = 32;

    private Material[][][] m_Voxels;

    public Chunk() {
        m_Voxels = new Material[DIM_X][DIM_Y][DIM_Z];
        clear();
    }

    public void setVoxel(int chunkX, int chunkY, int chunkZ, Material material) {
        m_Voxels[chunkX][chunkY][chunkZ] = material;
    }
    public Material getVoxel(int chunkX, int chunkY, int chunkZ) {
        return m_Voxels[chunkX][chunkY][chunkZ];
    }
    public void clear() {
        for(int x = 0; x < Chunk.DIM_X; x++) {
            for (int y = 0; y < Chunk.DIM_Y; y++) {
                for (int z = 0; z < Chunk.DIM_Z; z++) {
                    m_Voxels[x][y][z] = Material.AIR;
                }
            }
        }
    }
}
