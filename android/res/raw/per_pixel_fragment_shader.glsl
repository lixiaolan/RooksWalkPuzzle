precision mediump float;       	// Set the default precision to medium. We don't need as high of a 
								// precision in the fragment shader.

uniform sampler2D arrow_Texture;    // The input texture.
varying vec4 v_Color;          	// This is the color from the vertex shader interpolated across the 
  								// triangle per fragment.
varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.
uniform sampler2D text_Texture;
  
// The entry point for our fragment shader.
void main()                    		
{                              


	// Multiply the color by the diffuse illumination level and texture value to get final output color.
    mediump vec4 text_color = texture2D(text_Texture,v_TexCoordinate);
    //+(1.0-text_color.a)*v_Color;
    mediump vec4 arrow_color = texture2D(arrow_Texture, v_TexCoordinate);
                                      	
    gl_FragColor = arrow_color+text_color+(1.0-(arrow_color.a+text_color.a))*v_Color;//+text_color;
}                                                                     	

