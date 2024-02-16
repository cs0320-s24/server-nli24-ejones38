package edu.brown.cs.student.main.Cache;

/**
 * Enums that refer to different options for eviction policy. Currently, developers can choose to
 * use time after access based, size based, reference based, or no eviction policy.
 */
public enum EvictionPolicy {
  TIME,
  SIZE,
  REFERENCE,

  NONE
}
