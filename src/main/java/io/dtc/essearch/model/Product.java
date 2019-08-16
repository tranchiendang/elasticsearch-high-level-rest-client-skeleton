package io.dtc.essearch.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import lombok.Data;

/**
 * Product object represents product document in elasticsearch
 */
@Data
public class Product {
    
    /**
     * Need to be transient field
     */
    @NotNull
    private long id;

    private String name;
    
    private String slug;

    private String sku;

    private double price;

    private long quantity;

    private int rating;

    private String status;

    /**
     * Use this method to build properties for es index
     * @return XContentBuild es properties definition
     * @throws IOException
     */
    public static XContentBuilder buildBuilder() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.startObject("properties");
            {
                builder.startObject("id");
                {
                    builder.field("type", "long");
                }
                builder.endObject();
                //
                builder.startObject("name");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
                //
                builder.startObject("slug");
                {
                    builder.field("type", "text");
                    builder.field("analyzer", "hyphen_analyzer");
                    builder.field("search_analyzer", "standard");
                }
                builder.endObject();
                //
                builder.startObject("sku");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
                //
                builder.startObject("price");
                {
                    builder.field("type", "double");
                }
                builder.endObject();
                //
                builder.startObject("quantity");
                {
                    builder.field("type", "long");
                }
                builder.endObject();
                //
                //
                builder.startObject("rating");
                {
                    builder.field("type", "integer");
                }
                builder.endObject();
                //
                builder.startObject("status");
                {
                    builder.field("type", "text");
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();

        return builder;
    }

    /**
     * The list of fields will be used for searching
     * @return list of searching fields
     */
    public static List<String> getListFieldForSearch() {
        List<String> fieldList = new ArrayList<>();
        
        fieldList.add("name");
        fieldList.add("slug");
        fieldList.add("sku");

        return fieldList;
    }

    /**
     * Return index name
     * @param prefix prefix of index per env.
     * @return
     */
    public static String getIndexName(String prefix) {
        return new StringBuilder(prefix).append("product").toString();
    }
}