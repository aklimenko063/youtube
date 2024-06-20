package org.javaacademy.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "comment")
@NoArgsConstructor
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "comment_SEQ")
	private Long id;
	@Column
	private String text;

	@ManyToOne
	@JoinColumn(name = "video_id")
	@ToString.Exclude
	private Video video;

	@ManyToOne
	@JoinColumn(name = "tube_user_id")
	@ToString.Exclude
	private TubeUser tubeUserOwner;

	public Comment(String text, Video video, TubeUser tubeUserOwner) {
		this.text = text;
		this.video = video;
		this.tubeUserOwner = tubeUserOwner;
	}
}
