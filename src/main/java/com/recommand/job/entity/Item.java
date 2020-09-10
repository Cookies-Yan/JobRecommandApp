package com.recommand.job.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

/**
 * Item build()是jackson call的不是人为call的， 所以看不见
 */
@JsonIgnoreProperties(ignoreUnknown = true)  //不加的话，就一定要和API一一对应， 不然会出错
@JsonInclude(JsonInclude.Include.NON_NULL)  //自动过滤掉结果是 null的那条数据
public class Item {

    private String id;
    private String title;
    private String location;
    private String companyLogo;
    private String url;
    private String description;
    private Set<String> keywords;
    private boolean favorite;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("company_logo")
    public String getCompanyLogo() {
        return companyLogo;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public boolean getFavorite() {
        return favorite;
    }

   public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return favorite == item.favorite &&
                Objects.equals(id, item.id) &&
                Objects.equals(title, item.title) &&
                Objects.equals(location, item.location) &&
                Objects.equals(companyLogo, item.companyLogo) &&
                Objects.equals(url, item.url) &&
                Objects.equals(description, item.description) &&
                Objects.equals(keywords, item.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, location, companyLogo, url, description, keywords, favorite);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", keywords=" + keywords +
                ", favorite=" + favorite +
                '}';
    }

    public static class Builder {

        private String id;
        private String title;
        private String location;
        private String companyLogo;
        private String url;
        private String description;
        private Set<String> keywords;
        private boolean favorite;

        //当函数的返回值返回一个该类的对象时，就可以用这个函数的返回值继续调用该类或其子类的属性了
        //如：
        //getBook().getBook().getBook()
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder companyLogo(String companyLogo) {
            this.companyLogo = companyLogo;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder keywords(Set<String> keywords) {
            this.keywords = keywords;
            return this;
        }

        public Builder favorite(boolean favorite) {
            this.favorite = favorite;
            return this;
        }

        public Item build() {
            Item item = new Item();
            item.id = id;
            item.title = title;
            item.location = location;
            item.companyLogo = companyLogo;
            item.url = url;
            item.description = description;
            item.keywords = keywords;
            item.favorite = favorite;
            return item;
        }


    }
}


