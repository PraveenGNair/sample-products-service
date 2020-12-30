package functionaltests;

import com.intuit.karate.junit5.Karate;

public class AllFeaturesTests {

  // this class will automatically pick up all *.feature files
  // in src/test/java/** and even recurse sub-directories.
  @Karate.Test
  Karate testAll() {
    return Karate.run().relativeTo(getClass());
  }
}
