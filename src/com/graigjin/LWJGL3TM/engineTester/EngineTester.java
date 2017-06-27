package com.graigjin.LWJGL3TM.engineTester;

import com.graigjin.LWJGL3TM.entities.Camera;
import com.graigjin.LWJGL3TM.entities.Entity;
import com.graigjin.LWJGL3TM.models.RawModel;
import com.graigjin.LWJGL3TM.models.TexturedModel;
import com.graigjin.LWJGL3TM.renderEngine.Loader;
import com.graigjin.LWJGL3TM.renderEngine.OBJLoader;
import com.graigjin.LWJGL3TM.renderEngine.Renderer;
import com.graigjin.LWJGL3TM.shaders.StaticShader;
import com.graigjin.LWJGL3TM.textures.ModelTexture;
import org.joml.Vector3f;
import com.graigjin.LWJGL3TM.renderEngine.DisplayManager;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class EngineTester {

    public static void main (String[] args) throws IOException {
        DisplayManager.createDisplay(1200, 720, "Display");

        Loader loader = new Loader();

        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        RawModel model = OBJLoader.loadObjModel("stall", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("stall"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-5),0,0,0,1);

        Camera camera = new Camera();

        while(!glfwWindowShouldClose(DisplayManager.getWindow())) {
            entity.increaseRotation(0,1,0);
            camera.move();
            renderer.prepare();
            shader.start();
            shader.loadViewMatrix(camera);
            renderer.render(entity, shader);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();
    }

}
