package engine.util;

import engine.geometry.Geometry;
import engine.geometry.IndexedGeometry;
import org.lwjgl.opengl.GL11;

public class Draw3dUtils {

    public static Geometry cubeGeometry(float width, float height, float depth, float r, float g, float b) {
        float xMin = -width/2f;
        float xMax = -xMin;
        float yMin = -height/2f;
        float yMax = -yMin;
        float zMin = -depth/2f;
        float zMax = -zMin;

        float[] vertices = {
                //front face vertices
                xMin, yMin, zMax, 1f,   //0
                xMax, yMin, zMax, 1f,   //1
                xMax, yMax, zMax, 1f,   //2
                xMin, yMax, zMax, 1f,   //3
                //back face vertices
                xMin, yMax, zMin, 1f,   //4
                xMax, yMax, zMin, 1f,   //5
                xMax, yMin, zMin, 1f,   //6
                xMin, yMin, zMin, 1f    //7
        };

        float[] colors = {
                //front face colors
                r, g, b, 1f,
                r, g, b, 1f,
                r, g, b, 1f,
                r, g, b, 1f,
                //back face colors
                r, g, b, 1f,
                r, g, b, 1f,
                r, g, b, 1f,
                r, g, b, 1f
        };

        short[] indices = {
                //front face indices
                0, 1, 2,
                0, 2, 3,
                //top face indices
                3, 2, 5,
                3, 5, 4,
                //back face indices
                4, 5, 6,
                4, 6, 7,
                //bottom face indices
                7, 6, 1,
                7, 1, 0,
                //right face indices
                1, 6, 5,
                1, 5, 2,
                //left face indices
                7, 0, 3,
                7, 3, 4
        };

        return new IndexedGeometry(vertices, colors, indices);

    }

    public static Geometry sphereGeometry(float radius, int heightSegments, int widthSegments,  float r, float g, float b) {

        float a = 1f;   //alpha

        if (radius < 0) {
            radius = Math.abs(radius);
        }

        if (heightSegments < 2) {
            heightSegments = 2;
        }
        if (widthSegments < 3) {
            widthSegments = 3;
        }

        float thetaMin = 0f;
        float thetaMax = 360f;
        float deltaTheta = 360f / widthSegments;

        float phiMin = 0f;
        float phiMax = 180f;
        float deltaPhi = 180f / heightSegments;

        int numVertices = ((heightSegments - 1) * widthSegments) + 2; //plus 2 is for the top and bottom vertices
        int numTriangles = (widthSegments * 2) + ((heightSegments - 2) * widthSegments * 2);

        float[] vertices = new float[numVertices * 4];  //x, y, z, w
        float[] colors = new float[numVertices * 4];    //r, g, b, a
        short[] indices = new short[numTriangles * 3];      //one value for each vertex (3 per triangle)
        //TODO: handle the case where a short wont fit all of the indices we need.

        int i = 0;  // vertices/colors index

        for (float phi = phiMin; phi <= phiMax; phi += deltaPhi) {             // this is our rotation about x
            for (float theta = thetaMin; theta < thetaMax; theta += deltaTheta) {   // this is our rotation about z

                float x = radius * (float) (Math.sin(Math.toRadians(phi)) * Math.cos(Math.toRadians(theta)));
                float y = radius * (float) (Math.sin(Math.toRadians(phi)) * Math.sin(Math.toRadians(theta)));
                float z = radius * (float)  Math.cos(Math.toRadians(phi));
                float w = 1f;

                vertices[i  ] = x;
                vertices[i+1] = y;
                vertices[i+2] = z;
                vertices[i+3] = w;

                colors[i  ] = r;
                colors[i+1] = g;
                colors[i+2] = b;
                colors[i+3] = a;
                i += 4;

                if (phi == phiMin || phi == phiMax) {
                    //we don't want to rotate around z for these so we skip ahead.
                    break;
                }
            }
        }

        i = 0;
        int maxVertex = numVertices - 1;

        for (int currentVertex = 0; currentVertex < maxVertex; currentVertex++) {

            if (currentVertex == 0) {
                // top triangle fan:

                short index1 = 0;
                short index2 = 1;
                short index3 = 2;

                for (int j = 0; j < widthSegments; j += 1) {
                    if ( j == widthSegments - 1) {
                        //point back to the original index2 (which will always be '1')
                        index3 = 1;
                    }
                    indices[i  ] = index1;
                    indices[i+1] = index2;
                    indices[i+2] = index3;
                    i += 3;

                    // index1 is always 0 for the triangle fan but we must increment the others
                    index2++;
                    index3++;
                }
            } else if (currentVertex + widthSegments >= maxVertex){
                // bottom triangle fan

                if (currentVertex % widthSegments == 0) {

                    indices[i  ] = (short) currentVertex;
                    indices[i+1] = (short) maxVertex;
                    indices[i+2] = (short) (currentVertex + 1 - widthSegments);

                } else {
                    indices[i  ] = (short) currentVertex;
                    indices[i+1] = (short) maxVertex;
                    indices[i+2] = (short) (currentVertex + 1);
                }

                i += 3;

            } else {
                // do a square:
                if (currentVertex % widthSegments == 0) {
                    //first triangle
                    indices[i  ] = (short) currentVertex;
                    indices[i+1] = (short) (currentVertex + widthSegments);
                    indices[i+2] = (short) (currentVertex + 1);

                    //second triangle
                    indices[i+3] = (short) currentVertex;
                    indices[i+4] = (short) (currentVertex + 1);
                    indices[i+5] = (short) (currentVertex + 1 - widthSegments);
                } else {
                    //first triangle
                    indices[i  ] = (short) currentVertex;
                    indices[i+1] = (short) (currentVertex + widthSegments);
                    indices[i+2] = (short) (currentVertex + widthSegments + 1);

                    //second triangle
                    indices[i+3] = (short) currentVertex;
                    indices[i+4] = (short) (currentVertex + widthSegments + 1);
                    indices[i+5] = (short) (currentVertex + 1);
                }

                //next square
                i += 6;
            }
        }

        return new IndexedGeometry(vertices, colors, indices);
    }

    public static Geometry axisHelper(float length) {
        float xMin = 0;
        float xMax = length;
        float yMin = 0;
        float yMax = length;
        float zMin = 0;
        float zMax = length;

        float[] vertices = {
                //x-axis
                xMin, 0f, 0f, 1f,
                xMax, 0f, 0f, 1f,
                //y-axis
                0f, yMin, 0f, 1f,
                0f, yMax, 0f, 1f,
                //z-axis
                0f, 0f, zMin, 1f,
                0f, 0f, zMax, 1f
        };

        float[] colors = {
                //x-axis
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
                //y-axis
                0f, 1f, 0f, 1f,
                0f, 1f, 0f, 1f,
                //z-axis
                0f, 0f, 1f, 1f,
                0f, 0f, 1f, 1f
        };

        short[] indices = {
                //x-axis
                0, 1,
                //y-axis
                2, 3,
                //z-axis
                4, 5
        };

        return new IndexedGeometry(vertices, colors, indices, GL11.GL_LINES);
    }

    public static Geometry gridHelper(int size, int stepping) {

        int xMin = -size;
        int xMax = -xMin;

        int endNum = (int)((size/(float)stepping) * 2) + 1;
        int leftAndRight = endNum * 2;
        int topNum = (int)((size/(float)stepping) * 2) - 1;
        int topAndBottom = topNum * 2;
        int total = leftAndRight + topAndBottom;

        float[] vertices = new float[total * 4 + 16];
        float[] colors = new float[total * 4 + 16];
        short[] indices = new short[total + 4];

        int j = 0;
        float color = 0.2f;

        for (float i = xMin; i <= xMax; i += stepping) {

            //point 1
            vertices[j    ] = -size;
            vertices[j + 1] = 0f;
            vertices[j + 2] = i;
            vertices[j + 3] = 1f;
            colors[j    ] = color;
            colors[j + 1] = color;
            colors[j + 2] = color;
            colors[j + 3] = color;
            j += 4;

            //point 2
            vertices[j    ] = size;
            vertices[j + 1] = 0f;
            vertices[j + 2] = i;
            vertices[j + 3] = 1f;
            colors[j    ] = color;
            colors[j + 1] = color;
            colors[j + 2] = color;
            colors[j + 3] = color;
            j += 4;

            //point 3
            vertices[j    ] = i;
            vertices[j + 1] = 0f;
            vertices[j + 2] = -size;
            vertices[j + 3] = 1f;
            colors[j    ] = color;
            colors[j + 1] = color;
            colors[j + 2] = color;
            colors[j + 3] = color;
            j += 4;

            //point 4
            vertices[j    ] = i;
            vertices[j + 1] = 0f;
            vertices[j + 2] = size;
            vertices[j + 3] = 1f;
            colors[j    ] = color;
            colors[j + 1] = color;
            colors[j + 2] = color;
            colors[j + 3] = color;
            j += 4;
        }

        for (short i = 0; i < indices.length; ++i) {
            indices[i] = i;
        }

        return new IndexedGeometry(vertices, colors, indices, GL11.GL_LINES);
    }

}
