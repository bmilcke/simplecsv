package me.landmesser.simplecsv;

class Pair<T, U> {

  private final T first;
  private final U second;

  private Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }

  public static <R, S> Pair<R, S> of(R first, S second) {
    return new Pair<>(first, second);
  }

  public T getFirst() {
    return first;
  }

  public U getSecond() {
    return second;
  }
}
