package com.github.leeyazhou.scf.core.loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import com.github.leeyazhou.scf.core.util.FileUtil;

/**
 * A class for load jar
 * 
 */
public class SCFLoader extends URLClassLoader {

  public SCFLoader(URL[] urls) {
    super(urls);
  }

  public SCFLoader() {
    this(new URL[] {});
  }

  public void addURLFolder(String... dirs) throws Exception {
    List<String> jarList = FileUtil.getUniqueLibPath(dirs);
    for (String jar : jarList) {
      addURL(new URL("file", "", jar));
    }
  }


}
