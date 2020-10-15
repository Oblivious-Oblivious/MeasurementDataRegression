package mainengine;

/**
 * @class MainEngineFactory
 * @desc: A factory that produces MainEngines
 */
public class MainEngineFactory {
	public Engine createMainEngine(String engineType) {
		if(engineType.equals("MainEngine"))
            return new Engine();
		else
			return null;
	}
}
