package me.landmesser.simplecsv;

class Pair<T> {

  private final T first;
  private final T second;

  private Pair(T first, T second) {
    this.first = first;
    this.second = second;
  }

  public static <R> Pair<R> of(R first, R second) {
    return new Pair<>(first, second);
  }

  public T getFirst() {
    return first;
  }

  public T getSecond() {
    return second;
  }
}
