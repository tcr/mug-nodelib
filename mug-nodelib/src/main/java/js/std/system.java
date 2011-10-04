package js.std;

import mug.runtime.JSArray;
import mug.runtime.JSEnvironment;
import mug.runtime.JSModule;
import mug.runtime.JSObject;

public class system extends JSModule {
	@Override
	public JSObject load(JSEnvironment env) throws Exception {
		// args array
		JSArray args = new JSArray(env, 0);
		for (String arg : env.getCommandLineArgs())
			args.push(arg);
		
		// exports object
		final JSObject exports = new JSObject(env);
		exports.set("args", args);
		
		// running module returns exports object
		return exports;
	}
}
