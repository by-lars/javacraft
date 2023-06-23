package de.lars.javacraft.io;

import de.lars.javacraft.core.Game;
import org.joml.Vector2f;
import static org.lwjgl.glfw.GLFW.*;
public class Input {
    public static Vector2f getMovementAxies() {
        float forward     = glfwGetKey(Game.get().getWindow(), GLFW_KEY_W) == GLFW_PRESS ? 1 : 0;
        float backward    = glfwGetKey(Game.get().getWindow(), GLFW_KEY_S) == GLFW_PRESS ? 1 : 0;
        float left        = glfwGetKey(Game.get().getWindow(), GLFW_KEY_A) == GLFW_PRESS ? 1 : 0;
        float right       = glfwGetKey(Game.get().getWindow(), GLFW_KEY_D) == GLFW_PRESS ? 1 : 0;

        return new Vector2f(forward - backward, right - left);
    }
}
