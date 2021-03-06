package com.github.harverst.roper.model.common;

import com.github.harverst.roper.model.ScoreComponent;
import java.lang.Comparable;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONException;

import java.lang.Enum;
import static java.lang.Enum.valueOf;

/**
 * A ScoreComponent uses a component function to change the value of a score
 *
 * Implements compareTo using a protected precedence variable to determine
 * the order that the components will be applied.
 */
public abstract class BasicScoreComponent<P> implements ScoreComponent<P>
{
  /**
   * Used to determine the order in which the components are applied.
   */
  protected P phase;
  /**
   * The constructor sets the phase of the component.
   */
  public BasicScoreComponent(P aPhase)
  {
    phase = aPhase;
  }
  /**
   * Accepts the current value and returns the modified value.
   *
   * @param oldValue the value without this component
   * @return the value including this component
   */
  public abstract int composite(int oldValue);
  public P getPhase()
  {
    return phase;
  }
  
  /**
   *
   */
  public static <P> ScoreComponent<P>
    fromJson(Map<String, P> phaseMap, JSONObject obj)
  {
    try
    {
      // Needs some sort of phaseType
      int value = obj.getInt("value");
      P phase = phaseMap.get(obj.getString("phase"));
      switch(valueOf(BasicComponentType.class, obj.getString("type")))
      {
      case ADDITIVE:
        return new AdditiveScoreComponent(phase, value);
      case MULTIPLICATIVE:
        return new MultiplicativeScoreComponent(phase, value);
      case DIVISIVE:
        return new DivisiveScoreComponent(phase, value);
      default:
        return null;
      }
    }
    catch(JSONException e)
    {
      return null;
    }
  }
}

// The only way I can think to possibly make this not a compile-time enum 
// would be by use of class loaders to dynamically load all the component
// types

// I might actually change the design to work that way
enum BasicComponentType
{
  ADDITIVE,
  MULTIPLICATIVE,
  DIVISIVE
}

