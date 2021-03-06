package com.github.harverst.roper.model.common;

import com.github.harverst.roper.model.Character;
import com.github.harverst.roper.model.CharacterComponent;
import com.github.harverst.roper.model.ScoreComponent;
import com.github.harverst.roper.model.ScoreGroupType;

import java.lang.Enum;
import java.lang.String;
import java.lang.Class;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import static java.util.EnumSet.allOf;

import org.json.JSONObject;
import org.json.JSONException;

public class BasicCharacterComponent<P>
  implements CharacterComponent<P>
{
  private Map<ScoreGroupType, List<ScoreComponent<P> > > scoreComponents;
  public BasicCharacterComponent(
    Map<ScoreGroupType, List<ScoreComponent<P> > > components)
  {
    scoreComponents = components;
  }

  /**
   * Adds the group components comprising this character component.
   */
  public void composite(Character<P> c)
  {
    for(Map.Entry<ScoreGroupType, List<ScoreComponent<P> > > component : 
      scoreComponents.entrySet())
    {
      c.addGroupComponent(component.getKey(), component.getValue());
    }
  }
  
  /**
   * Removes the group components comprising this character component.
   */
  public void seperate(Character<P> c)
  {
    for(Map.Entry<ScoreGroupType, List<ScoreComponent<P> > > component : 
      scoreComponents.entrySet())
    {
      c.removeGroupComponent(component.getKey(), component.getValue());
    }
  }

  public static <P> CharacterComponent<P> fromJson(JSONObject obj, 
    Set<ScoreGroupType> scoreGroups, Map<String, P> phaseMap)
  {
    Map<ScoreGroupType, List<ScoreComponent<P> > > components = new HashMap();
    for(ScoreGroupType scoreGroup : scoreGroups)
    {
      try
      {
        List<ScoreComponent<P> > subList = new ArrayList();
        // Require that score groups have a name
        JSONObject subjson = obj.getJSONObject(scoreGroup.getName());
        // Require that score groups have a listing of their scores' names
        for(String scoreName : scoreGroup.getScoreNames())
        {
          //
          try
          {
            subList.add(BasicScoreComponent.fromJson(phaseMap, 
              subjson.getJSONObject(scoreName)));
          }
          catch(JSONException e)
          {
            // Add a null to hold the place in the list
            subList.add(null);
          }
        }
        components.put(scoreGroup, subList);
      }
      catch(JSONException e)
      {
        // Just not present, don't worry
      }
    }
    return new BasicCharacterComponent(components);
  }
  public static <P> CharacterComponent<P>
    fromFile(Set<ScoreGroupType> scoreGroups, List<P> phases, String source)
  {
    // TODO: implement
    return null;
  }
}

