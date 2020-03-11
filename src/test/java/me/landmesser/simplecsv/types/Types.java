package me.landmesser.simplecsv.types;

import me.landmesser.simplecsv.annotation.CSVDateFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class Types {

  private String string;
  private int primInt;
  private char primChar;
  private boolean primBoolean;

  private Integer integer;
  private Character character;
  private Boolean boleanVal;

  private BigDecimal bigDecimal;
  private LocalTime localTime;
  private LocalDate localDate;
  private LocalDateTime localDateTime;

  private TestEnum testEnum;

  private Date oldSchool;

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  public int getPrimInt() {
    return primInt;
  }

  public void setPrimInt(int primInt) {
    this.primInt = primInt;
  }

  public char getPrimChar() {
    return primChar;
  }

  public void setPrimChar(char primChar) {
    this.primChar = primChar;
  }

  public boolean isPrimBoolean() {
    return primBoolean;
  }

  public void setPrimBoolean(boolean primBoolean) {
    this.primBoolean = primBoolean;
  }

  public Integer getInteger() {
    return integer;
  }

  public void setInteger(Integer integer) {
    this.integer = integer;
  }

  public Character getCharacter() {
    return character;
  }

  public void setCharacter(Character character) {
    this.character = character;
  }

  public Boolean getBoleanVal() {
    return boleanVal;
  }

  public void setBooleanVal(Boolean boleanVal) {
    this.boleanVal = boleanVal;
  }

  public void setBoleanVal(Boolean boleanVal) {
    this.boleanVal = boleanVal;
  }

  public BigDecimal getBigDecimal() {
    return bigDecimal;
  }

  public void setBigDecimal(BigDecimal bigDecimal) {
    this.bigDecimal = bigDecimal;
  }

  public LocalTime getLocalTime() {
    return localTime;
  }

  public void setLocalTime(LocalTime localTime) {
    this.localTime = localTime;
  }

  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public TestEnum getTestEnum() {
    return testEnum;
  }

  public void setTestEnum(TestEnum testEnum) {
    this.testEnum = testEnum;
  }

  public Date getOldSchool() {
    return oldSchool;
  }

  public void setOldSchool(Date oldSchool) {
    this.oldSchool = oldSchool;
  }
}
