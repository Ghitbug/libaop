# libAOP

allprojects {
		repositories {
    
			maven { url 'https://jitpack.io' }
			
		}
	}
  
  
  dependencies {
  
	        implementation 'com.github.Ghitbug:libAOP:V1.0.1'
          
	}
  
aspectjx {
        include "自己项目的包名", "com.gh.libaop"
        //可加可不加，有些第三方库不加会编译会报错
        exclude 'com.google', 'com.ut', 'rxhttp', 'com.squareup', 'org.apache', 'versions.9'
}
