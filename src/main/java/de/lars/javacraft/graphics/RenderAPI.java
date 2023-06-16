package de.lars.javacraft.graphics;

public enum RenderAPI {
        NONE,
        OPENGL_4;

    static RenderAPI current() {
        return OPENGL_4;
    }
}
