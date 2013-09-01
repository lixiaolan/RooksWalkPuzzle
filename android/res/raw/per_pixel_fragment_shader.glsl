precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.

uniform sampler2D u_Texture;    // The input texture.
varying vec4 v_Color;          	// This is the color from the vertex shader interpolated across the 
  								// triangle per fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.
uniform sampler2D text_texture;
  
// The entry point for our fragment shader.
void main()                    		
{                              


	// Multiply the color by the diffuse illumination level and texture value to get final output color.
    mediump vec4 text_color = texture2D(text_texture,v_TexCoordinate);
    mediump vec4 o_color = texture2D(u_Texture, v_TexCoordinate);                                  	
    gl_FragColor = text_color*0.5+o_color*0.5;
}                                                                     	

