package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "video")
@NoArgsConstructor
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "video_SEQ")
	private Long id;
	@Column
	private String name;
	@Column
	private String description;

	@ManyToOne
	@JoinColumn(name = "tube_user_id")
	@ToString.Exclude
	private TubeUser tubeUserOwner;

	@OneToMany(mappedBy = "video")
	private List<Comment> commentList;

	public Video(String name, String description, TubeUser tubeUserOwner) {
		this.name = name;
		this.description = description;
		this.tubeUserOwner = tubeUserOwner;
	}
}
