package mp3player.durgesh.com.mp3player.bean;

import java.util.ArrayList;

/**
 * Created by rebelute13 on 19/2/18.
 */

public class CartItemsListBean {

    private String status,message;
    private ArrayList<CartListBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<CartListBean> getData() {
        return data;
    }

    public void setData(ArrayList<CartListBean> data) {
        this.data = data;
    }

    public class CartListBean{

        private String id;
        private String store_id;
        private String product_id;
        private String product_count;
        private String row_id;
        private String Payment_id;

        public String getModel_no_item_no() {
            return model_no_item_no;
        }

        public void setModel_no_item_no(String model_no_item_no) {
            this.model_no_item_no = model_no_item_no;
        }

        private String model_no_item_no;

        private String product_name,product_type,product_price,product_image_path;


        public String getPayment_id() {
            return Payment_id;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public void setPayment_id(String payment_id) {
            Payment_id = payment_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }


        public String getProduct_image_path() {
            return product_image_path;
        }

        public void setProduct_image_path(String product_image_path) {
            this.product_image_path = product_image_path;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_count() {
            return product_count;
        }

        public void setProduct_count(String product_count) {
            this.product_count = product_count;
        }

        public String getRow_id() {
            return row_id;
        }

        public void setRow_id(String row_id) {
            this.row_id = row_id;
        }

        @Override
        public String toString() {
            return "CartListBean{" +
                    "id='" + id + '\'' +
                    ", store_id='" + store_id + '\'' +
                    ", product_id='" + product_id + '\'' +
                    ", product_count='" + product_count + '\'' +
                    ", row_id='" + row_id + '\'' +
                    ", Payment_id='" + Payment_id + '\'' +
                    ", model_no_item_no='" + model_no_item_no + '\'' +
                    ", product_name='" + product_name + '\'' +
                    ", product_type='" + product_type + '\'' +
                    ", product_price='" + product_price + '\'' +
                    ", product_image_path='" + product_image_path + '\'' +
                    '}';
        }
    }

}
