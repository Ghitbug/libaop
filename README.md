# libAOP


allprojects {

		repositories {
		
			...
			maven { url 'https://jitpack.io' }
		}
}


dependencies {

	    implementation 'com.github.Ghitbug:libAOP:V1.0.0'
	    
}

// AOP 配置
aspectjx {

    // 排除一些第三方库的包名（Gson、 LeakCanary 和 AOP 有冲突）
    
    // 否则就会起冲突：ClassNotFoundException: Didn't find class on path: DexPathList
    
    exclude 'androidx', 'com.google', 'com.squareup', 'com.alipay', 'com.taobao',
            
	    'com.baidu','map','vi', 'org.apache', 'com.chad'
	    
}
