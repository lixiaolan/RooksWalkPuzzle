#include "LJJ_Rook.hpp"

extern "C" jstring Java_com_seventhharmonic_android_freebeeline_Board_stringFromJNI( JNIEnv* env, jobject thiz, jint rows, jint cols, jint length )
{
  RookBoard B(rows,cols,length);
  string a = B.print();

  return env->NewStringUTF(a.c_str());
}
