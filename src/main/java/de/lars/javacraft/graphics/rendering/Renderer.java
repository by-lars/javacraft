package de.lars.javacraft.graphics.rendering;
import de.lars.javacraft.graphics.api.Buffer;
import de.lars.javacraft.graphics.api.Shader;
import de.lars.javacraft.graphics.geometry.Mesh;
import de.lars.javacraft.world.Chunk;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {

    private static int m_RenderWidth;
    private static int m_renderHeight;

    private static float m_Fov;

    private static Shader m_ChunkShader;
    private static Buffer m_ChunkVerts;
    private static Buffer m_ChunkIndicies;

    private static Buffer m_Triangle;

    private static int m_Vao;

    public static void init(int width, int height) {

        m_RenderWidth = width;
        m_renderHeight = height;
        m_Fov = 90.0f;

        m_ChunkShader = Shader.fromFiles("shader/chunk.vert", "shader/chunk.frag");
        m_ChunkVerts = Buffer.createArrayBuffer(Chunk.DIM_X * Chunk.DIM_Y * Chunk.DIM_Z * 4 * 6);
        m_ChunkIndicies = Buffer.createIndexBuffer(Chunk.DIM_X * Chunk.DIM_Y * Chunk.DIM_Z * 4 * 6);

        m_Vao = glGenVertexArrays();
        glBindVertexArray(m_Vao);

        m_Triangle = Buffer.createArrayBuffer(9 * Float.BYTES);

        float[] triangle = {
                0.0f,  1.0f, 0.0f,
                -1.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f
        };

        m_Triangle.upload(triangle, 0);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES,0);
        glEnableVertexAttribArray(0);

        setRenderSize(width, height);

        Matrix4f view = new Matrix4f();
        view = view.identity();
        view = view.translate(0.0f,0.0f,-3.0f);
        setView(view);

        // Setup GL State
        glEnable(GL_DEPTH_TEST);
    }

    public static void shutdown() {

    }

    public static void setRenderSize(int width, int height) {
        if(width < 0 || height < 0) {
            return;
        }
        m_RenderWidth = width;
        m_renderHeight = height;

        glViewport(0, 0, width, height);
        updateProjectionMatrix();
    }

    public static void setFov(float fov) {
        m_Fov = fov;
        updateProjectionMatrix();
    }

    private static void updateProjectionMatrix() {
        Matrix4f projection = new Matrix4f().perspective((float)Math.toRadians(m_Fov), (float)m_RenderWidth / (float)m_renderHeight, 0.1f, 100.0f);
        m_ChunkShader.setMat4f("uProj", projection);
    }

    public static void setView(Matrix4f view) {
        m_ChunkShader.setMat4f("uView", view);
    }

    public static void renderChunk(Mesh mesh) {
        glBindVertexArray(m_Vao);

        m_ChunkVerts.upload(mesh.vertices, 0);
        m_ChunkIndicies.upload(mesh.indices, 0);
        m_ChunkShader.use();
        m_ChunkVerts.use();
        m_ChunkIndicies.use();

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES,0);
        glEnableVertexAttribArray(0);

         glDrawElements(GL_TRIANGLES, mesh.numIndices, GL_UNSIGNED_INT, 0);
    }

    public static void debugRenderTriangle() {
        glBindVertexArray(m_Vao);
        m_Triangle.use();
        m_ChunkShader.use();

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES,0);
        glEnableVertexAttribArray(0);

        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
