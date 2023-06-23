package de.lars.javacraft.core;

import de.lars.javacraft.graphics.api.Shader;
import de.lars.javacraft.graphics.api.Buffer;
import de.lars.javacraft.graphics.geometry.ChunkMesher;
import de.lars.javacraft.graphics.geometry.Mesh;
import de.lars.javacraft.graphics.rendering.Camera;
import de.lars.javacraft.graphics.rendering.Renderer;
import de.lars.javacraft.io.Input;
import de.lars.javacraft.world.Chunk;
import de.lars.javacraft.world.Material;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {
    private static Game s_Instance;
    private final long m_Window;
    private final Camera cam;
    private final Chunk chunk;
    public Game(long window) {
        s_Instance = this;
        m_Window = window;

        cam = new Camera(new Vector3f(0,0,0));
        chunk = new Chunk();

        Renderer.init(800, 600);
    }

    public static Game get() {
        return s_Instance;
    }

    public long getWindow() {
        return m_Window;
    }

    private void handleMouse() {
        try ( MemoryStack stack = stackPush() ) {
            DoubleBuffer pX = stack.mallocDouble(1);
            DoubleBuffer pY = stack.mallocDouble(1);

            glfwGetCursorPos(m_Window, pX, pY);

            cam.rotate((float)pX.get(), (float)pY.get());
        }
    }

    private void handleKeyboard() {
        Vector2f input = Input.getMovementAxies();
        Quaternionf rotation = cam.getOrientation().conjugate();
        Vector3f acceleration = rotation.transform(new Vector3f(input.y, 0, input.x * -1.0f));
        cam.translate(acceleration.mul(0.2f));
    }

    public void update() {
        handleMouse();
        handleKeyboard();
    }

    public void onResize(int width, int height) {
        Renderer.setRenderSize(width, height);
    }

    public void draw() {
        glClearColor(0.31f, 0.31f, 0.31f, 1.0f);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Renderer.setView(cam.getViewMatrix());
        double time = glfwGetTime();
        chunk.clear();
        for(int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                float height1 = Math.abs((float)Math.sin((float)(x)*0.01f + time));
                float height2 = Math.abs((float)Math.sin((float)(y)*0.02f + time));
                float height = (height1 + height2) * 16.0f;
                height = Math.min(height, 32);

                for(int z = 0; z < height; z++) {
                    chunk.setVoxel(x, z, y, Material.SOLID);
                }
            }
        }

        Mesh mesh = ChunkMesher.mesh(chunk);

        Renderer.renderChunk(mesh);
        Renderer.debugRenderTriangle();
        mesh.dispose();
    }
}
