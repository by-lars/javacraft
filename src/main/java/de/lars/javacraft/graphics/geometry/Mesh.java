package de.lars.javacraft.graphics.geometry;

import de.lars.javacraft.graphics.api.GPUResource;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh implements GPUResource {
    public FloatBuffer vertices;
    public IntBuffer indices;

    public int numVertices;
    public int numIndices;

    Mesh(int size) {
        vertices = MemoryUtil.memAllocFloat(size);
        indices = MemoryUtil.memAllocInt(size);
        numVertices = 0;
        numIndices = 0;
    }

    @Override
    public void dispose() {
        if(vertices != null) {
            MemoryUtil.memFree(vertices);
        }

        if(indices != null) {
            MemoryUtil.memFree(indices);
        }
    }
}
