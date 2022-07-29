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
public class PassengerListStructDTO {
  @JSONField(
      name = "passenger_id"
  )
  private String passenger_id;

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
      name = "passenger_group"
  )
  private Integer passenger_group;

  public void setPassenger_id(String passenger_id) {
    this.passenger_id = passenger_id;
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

  public void setPassenger_group(Integer passenger_group) {
    this.passenger_group = passenger_group;
  }

  public String getPassenger_id() {
    return this.passenger_id;
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

  public Integer getPassenger_group() {
    return this.passenger_group;
  }
}
