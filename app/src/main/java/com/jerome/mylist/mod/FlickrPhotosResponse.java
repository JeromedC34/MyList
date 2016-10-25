package com.jerome.mylist.mod;

import java.util.List;

public class FlickrPhotosResponse {
    private Photos photos;
    private String stat;

    public FlickrPhotosResponse() {
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public class Photos {
        private String page;
        private String pages;
        private String perpage;
        private String total;
        private List<Photo> photo;

        public Photos() {
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPages() {
            return pages;
        }

        public void setPages(String pages) {
            this.pages = pages;
        }

        public String getPerpage() {
            return perpage;
        }

        public void setPerpage(String perpage) {
            this.perpage = perpage;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<Photo> getPhoto() {
            return photo;
        }

        public void setPhoto(List<Photo> photo) {
            this.photo = photo;
        }

        public class Photo {
            private String id;
            private String owner;
            private String secret;
            private String server;
            private String farm;
            private String title;
            private String ispublic;
            private String isfriend;
            private String isfamily;

            public Photo() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public String getServer() {
                return server;
            }

            public void setServer(String server) {
                this.server = server;
            }

            public String getFarm() {
                return farm;
            }

            public void setFarm(String farm) {
                this.farm = farm;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getIspublic() {
                return ispublic;
            }

            public void setIspublic(String ispublic) {
                this.ispublic = ispublic;
            }

            public String getIsfriend() {
                return isfriend;
            }

            public void setIsfriend(String isfriend) {
                this.isfriend = isfriend;
            }

            public String getIsfamily() {
                return isfamily;
            }

            public void setIsfamily(String isfamily) {
                this.isfamily = isfamily;
            }
        }
    }
}