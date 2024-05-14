package com.example.assignment_individual;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.codingstuff.shoeapp.utils.model.ShoeCart;

import java.util.List;

@Dao
public interface CartDAO {

    @Insert
    void insertCartItem(ShoeCart shoeCart);

    @Query("SELECT * FROM shoe_cart")
    LiveData<List<ShoeCart>> getAllCartItems();

    @Delete
    void deleteCartItem(ShoeCart shoeCart);

    @Query("UPDATE shoe_cart SET quantity=:quantity WHERE id=:id")
    void updateQuantity(int id , int quantity);

    @Query("UPDATE shoe_cart SET totalItemPrice=:totalItemPrice WHERE id=:id")
    void updatePrice(int id , double totalItemPrice);

    @Query("DELETE FROM shoe_cart")
    void deleteAllItems();
}
