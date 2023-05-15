package pl.soltys.CookingBookApplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecipeDBModel {
    @Id
    private Integer API_ID;
    private String Name;
    private String Description;
    private String PictureURL;
}
