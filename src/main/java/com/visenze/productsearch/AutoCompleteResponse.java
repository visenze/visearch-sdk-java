package com.visenze.productsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visenze.common.util.ViJsonMapper;
import com.visenze.visearch.AutoCompleteResultItem;
import com.visenze.visearch.ResponseMessages;
import com.visenze.visearch.internal.InternalViSearchException;
import com.visenze.visearch.internal.http.ViSearchHttpResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hung on 24/11/23.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoCompleteResponse extends BaseProductSearchResponse {

    @JsonProperty("result")
    private List<AutoCompleteResultItem> result;

    /**
     * Delegated construction with a ViHttpResponse will automatically parse the
     * response into data members.
     *
     * Q: Why do we need to use a static function to create object instead of
     * calling constructor straight?
     * A: Because Java don't support self assignment. i.e. We cannot convert a
     * response json text body into T object from T's own constructor.
     *
     * @param response The ViHttpResponse received by calling ViHttpClient
     *                 api functions.
     * @return Product Search autocomplete response
     */
    public static AutoCompleteResponse fromResponse(ViSearchHttpResponse response) {
        try {
            return mapper.readValue(response.getBody(), AutoCompleteResponse.class);
        } catch(IOException e){
            throw new InternalViSearchException(ResponseMessages.PARSE_RESPONSE_ERROR, e.getMessage());
        }
    }

    public List<AutoCompleteResultItem> getResult() {
        return result;
    }

    public void setResult(List<AutoCompleteResultItem> result) {
        this.result = result;
    }
}
