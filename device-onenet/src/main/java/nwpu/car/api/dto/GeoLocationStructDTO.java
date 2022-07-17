package nwpu.car.api.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.Double;

@Data
@AllArgsConstructor
public class GeoLocationStructDTO {
  @JSONField(
          name = "lat"
  )
  private Double lat;

  @JSONField(
          name = "lon"
  )
  private Double lon;

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public void setLon(Double lon) {
    this.lon = lon;
  }

  public Double getLat() {
    return this.lat;
  }

  public Double getLon() {
      return this.lon;
    }

}
