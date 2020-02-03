package assistang.segmenter;

import java.util.HashMap;
import java.util.Map;

public class Segmenter {
  /**
   * word分词器
   * @Override
   */
  /*public Map<String, String> segMore(String text) {
      Map<String, String> map = new HashMap<>();
      for(SegmentationAlgorithm segmentationAlgorithm : SegmentationAlgorithm.values()){
          map.put(segmentationAlgorithm.getDes(), seg(text, segmentationAlgorithm));
      }
      return map;
  }
  private static String seg(String text, SegmentationAlgorithm segmentationAlgorithm) {
      StringBuilder result = new StringBuilder();
      for(Word word : Segmenter.segWithStopWords(text, segmentationAlgorithm)){
          result.append(word.getText()).append(" ");
      }
      return result.toString();
  }*/
}
