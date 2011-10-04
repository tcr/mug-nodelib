package js.std;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import mug.runtime.JSConcurrency;
import mug.runtime.JSEnvironment;
import mug.runtime.JSFunction;
import mug.runtime.JSModule;
import mug.runtime.JSNumber;
import mug.runtime.JSObject;
import mug.runtime.JSString;
import mug.runtime.JSUtils;
import mug.runtime.java.JSJavaUtils;
import mug.runtime.JSConcurrency.JSAsyncFunction;

public class fs extends JSModule
{	
	@Override
	public JSObject load(JSEnvironment env) throws Exception {
		/*
		 * read
		 */
		
		final JSFunction _readSync = new JSFunction (env) {
			@Override
			public Object invoke(Object ths, int argc, Object l0, Object l1, Object l2, Object l3, Object l4, Object l5, Object l6, Object l7, Object[] rest) throws Exception
			{
				// coerce path
				String path = JSUtils.asString(l0);
				// coerce options
				//[TODO] later
				
				// create file stream
			    FileInputStream stream = new FileInputStream(new File(path));
			    try {
			    	// read file
			        Reader reader = new BufferedReader(new InputStreamReader(stream, Charset.defaultCharset()));
			        StringBuilder builder = new StringBuilder();
			        char[] buffer = new char[8192];
			        int read;
			        while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
			            builder.append(buffer, 0, read);
			        }
			        
			        // return string object
			        return builder.toString();
			    } finally {
			        stream.close();
			    }        
			}
		};
		
		/*
		 * write
		 */
		
		final JSFunction _writeSync = new JSFunction (env) {
			@Override
			public Object invoke(Object ths, int argc, Object l0, Object l1, Object l2, Object l3, Object l4, Object l5, Object l6, Object l7, Object[] rest) throws Exception
			{
				// coerce path
				String path = JSUtils.asString(l0);
				// coerce options
				//[TODO] later
				
				byte[] data = (byte[]) JSJavaUtils.coerceJavaType(l1, byte[].class);
				// create file stream
			    FileOutputStream stream = new FileOutputStream(new File(path));
			    try {
			    	stream.write(data);
			    } finally {
			        stream.close();
			    }
				return null;
			}
		};
		
		/*
		 * rename
		 */
		
		final JSFunction _rename = new JSAsyncFunction(env) {
			public Runnable invokeAsync(Object ths, int argc, Object l0, Object l1, final Object l2, Object l3, Object l4, Object l5, Object l6, Object l7, Object[] rest) throws Exception {
				final String path1 = JSUtils.asString(l0);
				final String path2 = JSUtils.asString(l1);
				
				// rename file
				Exception e;
				try {
					(new File(path1)).renameTo(new File(path2));
				} catch (Exception e2) {
					e = e2;
				}
				// pass to callback
				return invokeSync((JSObject) l2, null, e);
			}				
		};
		
		final JSFunction _renameSync = new JSFunction(env) {
			public Object invoke(Object ths, int argc, Object l0, Object l1, final Object l2, Object l3, Object l4, Object l5, Object l6, Object l7, Object[] rest) throws Exception {
				final String path1 = JSUtils.asString(l0);
				final String path2 = JSUtils.asString(l1);
				
				// rename file
				(new File(path1)).renameTo(new File(path2));
				// return
				return null;
			}				
		};
		
		/*
		 * exports
		 */
		
		// exports object
		final JSObject exports = new JSObject(env) { {
			defineProperty("read", _readSync);
			defineProperty("write", _writeSync);
			defineProperty("rename", _rename);
			defineProperty("renameSync", _rename);
		} };
		
		// running module returns exports object
		return exports;
	}
}
