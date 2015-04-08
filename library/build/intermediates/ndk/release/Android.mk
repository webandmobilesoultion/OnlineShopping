LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := gpuimage-library
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	/Volumes/DATA/WORK/WORK/androidstudio/android-gpuimage-master/library/jni/yuv-decoder.c \

LOCAL_C_INCLUDES += /Volumes/DATA/WORK/WORK/androidstudio/android-gpuimage-master/library/jni
LOCAL_C_INCLUDES += /Volumes/DATA/WORK/WORK/androidstudio/android-gpuimage-master/library/src/release/jni

include $(BUILD_SHARED_LIBRARY)
