package engine.shader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {

    private ShaderUtils() {}

    public static int loadShader(String filename, int type) {
        StringBuilder shaderSource = readFile(filename);

        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, shaderSource);

        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Shader: " + filename + " failed to compile.");
            System.err.println(glGetShaderInfoLog(shaderId, GL_INFO_LOG_LENGTH));
        }

        return shaderId;
    }

    private static StringBuilder readFile(String filename) {
        StringBuilder fileContents = new StringBuilder();

        try {
            InputStream is = ShaderUtils.class.getClassLoader().getResourceAsStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                fileContents.append(line).append("\n");
            }
            reader.close();
        } catch (Exception e) {
            System.err.println("Could not read file: " + filename);
            e.printStackTrace();
            System.exit(-1);
        }

        return fileContents;
    }

}
