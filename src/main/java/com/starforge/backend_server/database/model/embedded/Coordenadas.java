package com.starforge.backend_server.database.model.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordenadas {

    @Column(name = "COORD_LAT_MISSAO")
    private Double latitude;

    @Column(name = "COORD_LNG_MISSAO")
    private Double longitude;
}
