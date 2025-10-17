-keepattributes SourceFile,LineNumberTable
-keepattributes Signature, InnerClasses, EnclosingMethod, KotlinMetadata
-keepattributes AnnotationDefault, *Annotation*

# 카카오 SDK 모델 클래스의 필드 보호
-keep class com.kakao.sdk.**.model.* { <fields>; }

# 도메인 모델 전체 보호
-keep class com.uwange.domain.model.** { *; }