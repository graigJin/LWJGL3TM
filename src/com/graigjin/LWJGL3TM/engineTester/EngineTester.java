package com.graigjin.LWJGL3TM.engineTester;

import com.graigjin.LWJGL3TM.entities.Camera;
import com.graigjin.LWJGL3TM.entities.Entity;
import com.graigjin.LWJGL3TM.models.RawModel;
import com.graigjin.LWJGL3TM.models.TexturedModel;
import com.graigjin.LWJGL3TM.renderEngine.Loader;
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

        float[] vertices = {
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f

        };

        float[] textureCoords = {

                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0


        };

        int[] indices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22

        };

        RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("default"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-5),0,0,0,1);

        Camera camera = new Camera();

        while(!glfwWindowShouldClose(DisplayManager.getWindow())) {
            entity.increaseRotation(1,1,0);
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
