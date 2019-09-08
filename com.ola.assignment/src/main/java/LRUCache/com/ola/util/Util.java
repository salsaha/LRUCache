package LRUCache.com.ola.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

public class Util {

	public static String readFully(Reader reader) throws IOException {
	    try {
	      StringWriter writer = new StringWriter();
	      char[] buffer = new char[1024];
	      int count;
	      while ((count = reader.read(buffer)) != -1) {
	        writer.write(buffer, 0, count);
	      }
	      return writer.toString();
	    } finally {
	      reader.close();
	    }
	  }
}
