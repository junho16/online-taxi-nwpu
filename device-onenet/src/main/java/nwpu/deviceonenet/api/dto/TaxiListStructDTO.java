package nwpu.deviceonenet.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.Double;
import java.lang.Integer;
import java.lang.String;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiListStructDTO {
  @JSONField(
      name = "taxi_id"
  )
  private String taxi_id;

  @JSONField(
      name = "taxi_name"
  )
  private String taxi_name;

  @JSONField(
      name = "lon"
  )
  private Double lon;

  @JSONField(
      name = "lat"
  )
  private Double lat;

  @JSONField(
      name = "state"
  )
  private Integer state;

  @JSONField(
      name = "speed"
  )
  private Double speed;

  @JSONField(
      name = "taxi_group"
  )
  private Integer taxi_group;

  public void setTaxi_id(String taxi_id) {
    this.taxi_id = taxi_id;
  }

  public void setTaxi_name(String taxi_name) {
    this.taxi_name = taxi_name;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public void setSpeed(Double speed) {
    this.speed = speed;
  }

  public void setTaxi_group(Integer taxi_group) {
    this.taxi_group = taxi_group;
  }

  public String getTaxi_id() {
    return this.taxi_id;
  }

  public String getTaxi_name() {
    return this.taxi_name;
  }

  public Double getLon() {
    return this.lon;
  }

  public Double getLat() {
    return this.lat;
  }

  public Integer getState() {
    return this.state;
  }

  public Double getSpeed() {
    return this.speed;
  }

  public Integer getTaxi_group() {
    return this.taxi_group;
  }
}
