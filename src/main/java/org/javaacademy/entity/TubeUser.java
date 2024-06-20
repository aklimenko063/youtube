package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "tube_user")
@NoArgsConstructor
public class TubeUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "tube_user_SEQ")
	private Long id;
	@Column(name = "nick_name")
	private String nickName;

	@OneToMany(mappedBy = "tubeUserOwner")
	private List<Video> videoList;

	@OneToMany(mappedBy = "tubeUserOwner")
	private List<Comment> commentList;

	public TubeUser(String nickName) {
		this.nickName = nickName;
	}
}
