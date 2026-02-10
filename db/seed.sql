USE ec_site;

INSERT INTO goods (id, code, name, description, price, category, image_url, created_at, updated_at) VALUES
  (1, 'ZAK-001', '木製スプーンセット',  '天然木を使用した優しい口当たりのスプーンセットです。',  1200, 'キッチン雑貨', '/assets/images/goods/zak-001.jpg', NOW(), NOW()),
  (2, 'ZAK-002', '耐熱ガラスマグ',  '電子レンジ対応のシンプルな耐熱ガラスマグ。',  1500, '食器', '/assets/images/goods/zak-002.jpg', NOW(), NOW()),
  (3, 'ZAK-003', 'コットンランチョンマット',  'ナチュラルカラーのコットン素材ランチョンマット。',  800, 'ファブリック', '/assets/images/goods/zak-003.jpg', NOW(), NOW()),
  (4, 'ZAK-004', '陶器フラワーベース',  'シンプルなデザインでどんな部屋にも合う花瓶。',  2800, 'インテリア', '/assets/images/goods/zak-004.jpg', NOW(), NOW()),
  (5, 'ZAK-005', '折りたたみ収納ボックス',  '使わない時は折りたためる便利な収納ボックス。',  2200, '収納・小物', '/assets/images/goods/zak-005.jpg', NOW(), NOW()),
  (6, 'ZAK-006', 'シリコン調理スプーン',  '耐熱性に優れたシリコン製の調理用スプーン。',  1000, 'キッチン雑貨', '/assets/images/goods/zak-006.jpg', NOW(), NOW()),
  (7, 'ZAK-007', '北欧柄クッションカバー',  '北欧テイストの柄がおしゃれなクッションカバー。',  1800, 'ファブリック', '/assets/images/goods/zak-007.jpg', NOW(), NOW()),
  (8, 'ZAK-008', '陶器プレート 20cm',  '日常使いしやすいサイズの陶器プレート。',  1600, '食器', '/assets/images/goods/zak-008.jpg', NOW(), NOW()),
  (9, 'ZAK-009', 'LEDテーブルランプ',  '柔らかな光で空間を演出するテーブルランプ。',  4500, 'インテリア', '/assets/images/goods/zak-009.jpg', NOW(), NOW()),
  (10, 'ZAK-010', 'デスク小物トレー',  '文房具や小物を整理できるデスク用トレー。',  1300, '収納・小物', '/assets/images/goods/zak-010.jpg', NOW(), NOW());

INSERT INTO stocks (good_id, quantity, updated_at) VALUES
  (1, 50, NOW()),
  (2, 30, NOW()),
  (3, 100, NOW()),
  (4, 20, NOW()),
  (5, 40, NOW()),
  (6, 60, NOW()),
  (7, 25, NOW()),
  (8, 35, NOW()),
  (9, 15, NOW()),
  (10, 80, NOW());
