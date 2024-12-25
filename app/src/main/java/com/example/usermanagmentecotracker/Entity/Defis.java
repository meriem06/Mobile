package com.example.usermanagmentecotracker.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "defis",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("userId")}
)
public class Defis {
    @PrimaryKey(autoGenerate = true)
    private long idDefi;

    private String descriptionDefi;
    private Boolean isCompleted;

    private int userId;

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public Defis() {}

    public Defis(String descriptionDefi, int userId , Boolean isCompleted) {
        this.descriptionDefi = descriptionDefi;
        this.userId = userId;
        this.isCompleted = isCompleted;
    }

    public long getIdDefi() { return idDefi; }
    public void setIdDefi(long idDefi) { this.idDefi = idDefi; }
    public String getDescriptionDefi() { return descriptionDefi; }
    public void setDescriptionDefi(String descriptionDefi) { this.descriptionDefi = descriptionDefi; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

}
