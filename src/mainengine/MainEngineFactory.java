package mainengine;

/**
 * @class MainEngineFactory
 * @brief A factory that produces MainEngines
 */
public class MainEngineFactory {
	public Engine createMainEngine(String engineType) {
		if(engineType.equals("MainEngine"))
            return new Engine();
		else
			return null;
	}
}
