package com.gemastik.dentistsmile.ui.maps;

import com.gemastik.dentistsmile.data.model.maps.DataPlace;

public interface MapsInterface {
    void onDirection(DataPlace data);

    void onShare(DataPlace data);

    void onClick(DataPlace data);
}
