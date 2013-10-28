precision lowp float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.

uniform sampler2D texture_0;  // The input texture.
varying vec4 v_Color;          	// This is the color from the vertex shader interpolated across the 
  								// triangle per fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.
uniform sampler2D texture_1;
  
// The entry point for our fragment shader.
void main()                    		
{                              


	// Multiply the color by the diffuse illumination level and texture value to get final output color.
   	lowp vec4 color_0 = texture2D(texture_0,v_TexCoordinate);
    lowp vec4 color_1 = texture2D(texture_1, v_TexCoordinate);
    gl_FragColor = color_0+color_1+(1.0-(color_0.a+color_1.a))*v_Color;
}                                                                     	

