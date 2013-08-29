#include "LJJ_Rook.hpp"

extern "C" jstring Java_com_example_android_opengl_Board_stringFromJNI( JNIEnv* env, jobject thiz )
{
  RookBoard B(6,6,10);
  string a = B.print();

  return env->NewStringUTF(a.c_str());
}
