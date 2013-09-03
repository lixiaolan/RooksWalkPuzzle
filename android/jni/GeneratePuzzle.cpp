#include "LJJ_Rook.hpp"

extern "C" jstring Java_com_example_android_opengl_Board_stringFromJNI( JNIEnv* env, jobject thiz, jint rows, jint cols, jint length )
{
  RookBoard B(rows,cols,length);
  string a = B.print();

  return env->NewStringUTF(a.c_str());
}
