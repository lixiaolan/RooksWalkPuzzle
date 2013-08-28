LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := GeneratePuzzle
LOCAL_SRC_FILES := GeneratePuzzle.cpp LJJ_Rook.cpp

include $(BUILD_SHARED_LIBRARY)
